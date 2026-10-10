@echo off
setlocal
cd /d "%~dp0"
echo ============================================
echo NEXUS Backend - Local development
echo ============================================
echo Local mode uses a persistent embedded database.
echo Local mode uses H2. For Aiven, run run-aiven.ps1 and enter your Aiven connection details.
echo Backend health: http://localhost:8081/api/health
echo.
curl.exe -fsS --max-time 2 http://localhost:8081/api/health >nul 2>&1
if not errorlevel 1 (
  echo NEXUS backend is already running. Reusing the existing instance.
  endlocal
  exit /b 0
)
powershell -NoProfile -Command "$client = New-Object Net.Sockets.TcpClient; try { $client.Connect('127.0.0.1', 8081); exit 0 } catch { exit 1 } finally { $client.Dispose() }" >nul 2>&1
if not errorlevel 1 (
  echo ERROR: Port 8081 is occupied, but the NEXUS health endpoint is not ready.
  echo Wait for the existing backend to finish starting, then check:
  echo http://localhost:8081/api/health
  endlocal
  exit /b 1
)
call mvn spring-boot:run
endlocal
