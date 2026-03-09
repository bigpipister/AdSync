import request_prod from "@/utils/request_prod";

const PwdAPI = {
  getPage(data: PwdDiffListQuery) {
    return request_prod<any, PwdDiffResult>({
      url: "/pwdDiffList",
      method: "post",
      data: data,
      headers: {
        "Content-Type": "application/json",
      },
    });
  },

};

export default PwdAPI;

export interface PwdDiffListQuery {
  page: number;
  size: number;
}

export interface PwdDiffResult {
  result?: string;
  message?: string;
  total?: string;
  data?: PwdDiff[];
}

export interface PwdDiff {
  samaccountname?: string;
  sn?: string;
  dn?: string;
  innerTimestamp?: string;
  externalTimestamp?: string;
}
