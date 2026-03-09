import request_prod from "@/utils/request_prod";

const DecryptAPI = {
  decryptPwd(data: DecryptPwdQuery) {
    return request_prod<any, DecryptPwdResult>({
      url: "/decryptPwd",
      method: "post",
      data: data,
      headers: {
        "Content-Type": "application/json",
      },
    });
  },

};

export default DecryptAPI;

export interface DecryptPwdQuery {
  cn: string;
  encryptPwd: string;
}

export interface DecryptPwdResult {
  result?: string;
  message?: string;
  data?: string;
}

