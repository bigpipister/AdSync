import request_prod from "@/utils/request_prod";

const AuditAPI = {
  getPage(data: AuditListQuery) {
    return request_prod<any, AuditLogsResult>({
      url: "/auditLogs",
      method: "post",
      data: data,
      headers: {
        "Content-Type": "application/json",
      },
    });
  },

};

export default AuditAPI;

export interface AuditListQuery {
  page: number;
  size: number;
  region: string;
  activitydisplayname: string;
  targetresources: string;
  startTime: string;
  endTime: string
}

export interface AuditLogsResult {
  result?: string;
  message?: string;
  total?: string;
  data?: AuditLogs[];
}

export interface AuditLogs {
  region?: string;
  activitydisplayname?: string;
  targetresources?: string;
  activitydatetime?: string;
  additionaldetails?: string;
}
