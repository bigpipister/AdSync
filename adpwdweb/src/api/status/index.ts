import request_prod from "@/utils/request_prod";

const StatusAPI = {
  getStatusLogs() {
    return request_prod<any, StatusLogsResult>({
      url: "/statusLogs",
      method: "get",
    });
  },

};

export default StatusAPI;

export interface StatusLogsResult {
  result?: string;
  message?: string;
  total?: string;
  data?: StatusLogs[];
}

export interface StatusLogs {
  region?: string;
  jobname?: string;
  dn?: string;
  displayname?: string;
  running?: boolean;
  health?: string;
  startdatetime?: string;
  durationseconds?: number;
}
