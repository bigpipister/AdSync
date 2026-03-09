import request_prod from "@/utils/request_prod";

const PropertiesAPI = {
  getPage(data: PropertiesDiffListQuery) {
    return request_prod<any, PropertiesDiffResult>({
      url: "/propertiesDiffList",
      method: "post",
      data: data,
      headers: {
        "Content-Type": "application/json",
      },
    });
  },

};

export default PropertiesAPI;

export interface PropertiesDiffListQuery {
  page: number;
  size: number;
}

export interface PropertiesDiffResult {
  result?: string;
  message?: string;
  total?: string;
  data?: PropertiesDiff[];
}

export interface PropertiesDiff {
  samaccountname?: string;
  sn?: string;
  dn?: string;
  innerProperties?: string;
  externalProperties?: string;
}
