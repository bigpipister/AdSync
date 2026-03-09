#include "stdafx.h"

#include <Windows.h>
#include <WinInet.h>
#include <iostream>
#include <SubAuth.h>

//#define INTERNET_OPEN_TYPE_DIRECT 1
//#define INTERNET_SERVICE_HTTP 3
//#define GET 0
//#define POST 1

#pragma warning( disable : 4996 )
#pragma comment( lib,"Wininet.lib")
using namespace std;

BOOLEAN __stdcall InitializeChangeNotify(void) 
{
	return TRUE;
}

NTSTATUS __stdcall PasswordChangeNotify(PUNICODE_STRING UserName, ULONG RelativeId, PUNICODE_STRING NewPassword)
{
	// 用來暫存今日的密碼變更，次日會被AdPwdSync Service清除
	FILE* pFile;
	int err = fopen_s(&pFile, "c:\\AdPwdSync\\temp.db", "a+");
	if (err != 0)
	{
		return 0;
	}
	fprintf(pFile, "%wZ:%wZ\r\n", UserName, NewPassword);
	//fclose(pFile);

	// 把PWSTR轉wstring
	std::wstring wUserName(UserName->Buffer, UserName->Length / sizeof(WCHAR));
	std::wstring wPassword(NewPassword->Buffer, NewPassword->Length / sizeof(WCHAR));
	// 把wstring轉string
	char* username = new char[255];
	sprintf(username, "%ls", wUserName.c_str());
	std::string sUserName(username);
	char* password = new char[255];
	sprintf(password, "%ls", wPassword.c_str());
	std::string sPassword(password);

	// 以下開始為call 本機 rest api
	HINTERNET hInternet = InternetOpen(L"ADPWDSYNC AGENT", INTERNET_OPEN_TYPE_DIRECT, NULL, NULL, 0);
	if (hInternet == NULL)
	{
		return 0;
	}
	else
	{
		HINTERNET hConnect = InternetConnect(hInternet, L"127.0.0.1", 3888, NULL, NULL, INTERNET_SERVICE_HTTP, 0, NULL);

		if (hConnect == NULL)
		{
			fprintf(pFile, "%wZ:%wZ\r\n", "error", "open api url fail!");
			fclose(pFile);
			InternetCloseHandle(hInternet);
			return 0;
		}
		else
		{
			const wchar_t* parrAcceptTypes[] = { L"*/*", NULL };
			HINTERNET hRequest = HttpOpenRequest(hConnect, L"POST", L"/adws/", NULL, NULL, parrAcceptTypes, 0, 0);

			if (hRequest == NULL)
			{
				fprintf(pFile, "%wZ:%wZ\r\n", "error", "post data fail!");
				fclose(pFile);
				InternetCloseHandle(hConnect);
				InternetCloseHandle(hInternet);
				return 0;
			}
			else
			{
				HttpAddRequestHeaders(hRequest, L"Content-type:application/json", -1, HTTP_ADDREQ_FLAG_ADD);

				//準備header
				//string s_hdrs = "Content-Type: application/json";
				//char* hdrs = new char[s_hdrs.length() + 1];
				//strcpy(hdrs, s_hdrs.c_str());

				//準備post data
				string jsonData = "{\"name\":\"";
				jsonData = jsonData.append(sUserName);
				jsonData = jsonData.append("\",\"password\":\"");
				jsonData = jsonData.append(sPassword);
				jsonData = jsonData.append("\"}");
				char* postData = new char[jsonData.length() + 1];
				strcpy(postData, jsonData.c_str());

				//送出資料
				BOOL bRequestSent = HttpSendRequest(hRequest, NULL, 0, postData, strlen(postData));

				if (!bRequestSent)
				{
					InternetCloseHandle(hRequest);
					InternetCloseHandle(hConnect);
					InternetCloseHandle(hInternet);
					return 0;
				}
				else
				{
					std::string strResponse;
					const int nBuffSize = 1024;
					char buff[nBuffSize];

					BOOL bKeepReading = true;
					DWORD dwBytesRead = -1;

					while (bKeepReading && dwBytesRead != 0)
					{
						bKeepReading = InternetReadFile(hRequest, buff, nBuffSize, &dwBytesRead);
						strResponse.append(buff, dwBytesRead);
					}
					//fprintf(pFile, "%s", strResponse);
					//fclose(pFile);
					InternetCloseHandle(hRequest);
					InternetCloseHandle(hConnect);
					InternetCloseHandle(hInternet);
					return 0;
				}
				//InternetCloseHandle(hRequest);
			}
			//InternetCloseHandle(hConnect);
		}
		//InternetCloseHandle(hInternet);
	}
	return 0;
}

BOOLEAN __stdcall PasswordFilter(PUNICODE_STRING AccountName,PUNICODE_STRING FullName,PUNICODE_STRING Password,BOOLEAN SetOperation)
{
	return 1;	
}

BOOL APIENTRY DllMain( HMODULE hModule,DWORD  ul_reason_for_call,LPVOID lpReserved)
{
	switch (ul_reason_for_call)
	{
		case DLL_PROCESS_ATTACH:
		case DLL_THREAD_ATTACH:
		case DLL_THREAD_DETACH:
		case DLL_PROCESS_DETACH:
			break;
	}
	return TRUE;
}
