#!/bin/bash
# 將「已測試過」的 Jenkins image 以 buildah 壓成單層（內容不變），輸出 OCI archive
# 對應正式 build_command.txt：buildah --format docker --squash + podman save --format oci-archive
#   tools/build-squash-oci.sh
set -euo pipefail
cd "$(dirname "$0")/.."
SRC=adsync/jenkins:lts-jdk21-corretto
TAG=docker.io/jenkins/jenkins:latest
OUT=$PWD/export
mkdir -p "$OUT"
rm -f "$OUT/jenkins.oci" "$OUT/jenkins.oci.sha256" "$OUT/src.tar"

echo "[1/4] 匯出測試過的 image: $SRC ($(docker image inspect -f '{{.Id}}' $SRC | cut -c1-19))"
docker save -o "$OUT/src.tar" "$SRC"

echo "[2/4] buildah: from -> commit --squash -> push oci-archive"
docker run --rm --privileged -v "$OUT:/out" quay.io/buildah/stable:latest bash -ec "
  buildah --version
  c=\$(buildah --storage-driver vfs from docker-archive:/out/src.tar)
  buildah --storage-driver vfs commit --format docker --squash --rm \"\$c\" $TAG >/dev/null
  buildah --storage-driver vfs inspect --type image -f 'layers={{len .OCIv1.RootFS.DiffIDs}} user={{.OCIv1.Config.User}} entrypoint={{.OCIv1.Config.Entrypoint}}' $TAG
  buildah --storage-driver vfs push $TAG oci-archive:/out/jenkins.oci:$TAG
"
rm -f "$OUT/src.tar"

echo "[3/4] 檢查 archive"
tar -tf "$OUT/jenkins.oci" | grep -E '^(oci-layout|index.json)$'
echo "size: $(du -h "$OUT/jenkins.oci" | cut -f1)"
(cd "$OUT" && sha256sum jenkins.oci | tee jenkins.oci.sha256)

echo "[4/4] 載回 docker 並換給測試環境使用（舊的多層版留為 :layered）"
docker tag "$SRC" adsync/jenkins:layered
docker rmi "$TAG" >/dev/null 2>&1 || true
docker load -i "$OUT/jenkins.oci" | tail -1
docker tag "$TAG" "$SRC"
echo "loaded layers: $(docker image inspect -f '{{len .RootFS.Layers}}' "$TAG")"
