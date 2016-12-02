#include <windows.h>
#include <stdio.h>
#include <process.h>
#include "resource.h"

void createTempJar(int name, int type, const char* toSave)
{
    HRSRC rc = FindResource(NULL, MAKEINTRESOURCE(name), MAKEINTRESOURCE(type));
    if (rc == NULL) {
		printf("Resource not found\n");
		return;
	}
    HGLOBAL rcData = LoadResource(NULL, rc);
    
    char* DataBuffer;
    DataBuffer = (char*) LockResource(rcData);
    DWORD dwBytesToWrite = SizeofResource(NULL, rc);
    DWORD dwBytesWritten = 0;
    
    HANDLE hFile = CreateFile( toSave,                 // name of the write
                               GENERIC_WRITE,          // open for writing
                               0,                      // do not share
                               NULL,                   // default security
                               CREATE_ALWAYS,          // create new file only
                               FILE_ATTRIBUTE_NORMAL,  // normal file
                               NULL);                  // no attr. template

    if (hFile == INVALID_HANDLE_VALUE) {
		printf("Error opening file");
		return;
	}

    if (FALSE == WriteFile(hFile, DataBuffer, dwBytesToWrite, &dwBytesWritten, NULL)) {
		printf("Error writing file");
		return;
	}
	
	CloseHandle(hFile);
}

void runInNewProcess(char* command) 
{
	STARTUPINFO si;
    PROCESS_INFORMATION pi;

    ZeroMemory( &si, sizeof(si) );
    si.cb = sizeof(si);
    ZeroMemory( &pi, sizeof(pi) );

    // Start the child process. 
    if( !CreateProcess(
		NULL,   // No module name (use command line)
        command,        // Command line
        NULL,           // Process handle not inheritable
        NULL,           // Thread handle not inheritable
        FALSE,          // Set handle inheritance to FALSE
        0,              // No creation flags
        NULL,           // Use parent's environment block
        NULL,           // Use parent's starting directory 
        &si,            // Pointer to STARTUPINFO structure
        &pi )           // Pointer to PROCESS_INFORMATION structure
    ) 
    {
        printf( "CreateProcess failed (%d).\n", GetLastError() );
        return;
    }

    // Wait until child process exits.
    WaitForSingleObject( pi.hProcess, INFINITE );

    // Close process and thread handles. 
    CloseHandle( pi.hProcess );
    CloseHandle( pi.hThread );
    
    return;
}


int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow)
{
	//get temp path
	char path[250];
	GetTempPath(250, path);
	const char* filename = "PathFinder.jar";
	
	//build total path
	char* totalpath = malloc(strlen(filename) + strlen(path));
	strcpy(totalpath, path);
	strcat(totalpath, filename);
	
	//export jar from resource
    createTempJar(ID_MYJARFILE, JARFILE, totalpath);
    
    //open jar
    const char* command = "javaw -jar ";
    char* totalcommand = malloc(strlen(command) + strlen(totalpath));
    strcpy(totalcommand, command);
    strcat(totalcommand, totalpath);
    
    //run
    runInNewProcess(totalcommand);
    
    //remove temp jar
    if(remove(totalpath) < 0) {
		printf("error opening file");
		return -1;
	}

	return 0;
}

