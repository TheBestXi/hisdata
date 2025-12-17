@echo off
setlocal

cd /d "%~dp0"

if not exist "his-frontend\package.json" (
  echo [ERROR] Cannot find "his-frontend\package.json".
  echo Please place this script in the repository root folder.
  pause
  exit /b 1
)

cd /d "%~dp0his-frontend"

where node >nul 2>nul
if errorlevel 1 (
  echo [ERROR] Node.js is not installed or not in PATH.
  echo Install Node.js from https://nodejs.org/ and try again.
  pause
  exit /b 1
)

where npm >nul 2>nul
if errorlevel 1 (
  echo [ERROR] npm is not available.
  echo Please reinstall Node.js and try again.
  pause
  exit /b 1
)

set "VITE_USE_MOCK=true"

if not exist "node_modules\" (
  echo Installing dependencies...
  call npm install
  if errorlevel 1 (
    echo [ERROR] npm install failed.
    pause
    exit /b 1
  )
)

echo Starting frontend dev server...
echo If no error appears, open: http://localhost:5173/
call npm run dev

pause

