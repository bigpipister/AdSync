import request_prod from "@/utils/request_prod";

const EventAPI = {
  getPage(data: EventListQuery) {
    return request_prod<any, EventLogsResult>({
      url: "/eventLogs",
      method: "post",
      data: data,
      headers: {
        "Content-Type": "application/json",
      },
    });
  },

};

export default EventAPI;

export interface EventListQuery {
  page: number;
  size: number;
  region: string;
  jobname: string;
  dn: string;
  keyword: string;
}

export interface EventLogsResult {
  result?: string;
  message?: string;
  total?: string;
  data?: EventLogs[];
}

export interface EventLogs {
  region?: string;
  jobname?: string;
  dn?: string;
  activitydatetime?: string;
  exceptioncontent?: string;
  exceptionhash?: string;
}
