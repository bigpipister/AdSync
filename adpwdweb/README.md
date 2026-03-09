## 環境準備

| 環境                 | 名稱版本                                                     | 下載地址                                                     |
| -------------------- | :----------------------------------------------------------- | ------------------------------------------------------------ |
| **開發工具**         | VSCode    | [下載](https://code.visualstudio.com/Download)           |
| **運行環境**         | Node ≥18 (其中 20.6.0 版本不可用)    | [下載](http://nodejs.cn/download)                        |


## 項目啟動

```bash
# 安裝 pnpm
npm install pnpm -g

# 設置鏡像源(可忽略)
pnpm config set registry https://registry.npmmirror.com

# 安裝依賴
pnpm install

# 啟動運行
pnpm run dev
```

## 項目部署

```bash
# 項目打包
pnpm run build

# 上傳文件至遠程服務器
將本地打包生成的 dist 目錄下的所有文件拷貝至服務器的 /usr/share/nginx/html 目錄。

# nginx.cofig 配置
server {
	listen     80;
	server_name  localhost;
	location / {
			root /usr/share/nginx/html;
			index index.html index.htm;
	}
	# 反向代理配置
	location /prod-api/ {
      # api.youlai.tech 替換後端API地址，注意保留後面的斜槓 /
      proxy_pass http://api.youlai.tech/; 
	}
}
```

## 本地Mock

項目同時支持在線和本地 Mock 接口，默認使用線上接口，如需替換為 Mock 接口，修改文件 `.env.development` 的 `VITE_MOCK_DEV_SERVER` 為  `true` **即可**。

## 注意事項

- **自動導入插件自動生成默認關閉**

  模板項目的組件類型聲明已自動生成。如果添加和使用新的組件，請按照圖示方法開啟自動生成。在自動生成完成後，記得將其設置為 `false`，避免重複執行引發衝突。

  ![](https://foruda.gitee.com/images/1687755823137387608/412ea803_716974.png)

- **項目啟動瀏覽器訪問空白**

  請升級瀏覽器嘗試，低版本瀏覽器內核可能不支持某些新的 JavaScript 語法，比如可選鏈操作符 `?.`。

- **項目同步倉庫更新升級**

  項目同步倉庫更新升級之後，建議 `pnpm install` 安裝更新依賴之後啟動 。

- **項目組件、函數和引用爆紅**

	重啟 VSCode 嘗試
  