$ErrorActionPreference = 'Stop'

function Test-PortInUse {
    param([int]$Port)

    $client = New-Object System.Net.Sockets.TcpClient
    try {
        $client.Connect('127.0.0.1', $Port)
        return $true
    } catch {
        return $false
    } finally {
        $client.Dispose()
    }
}

if (Test-PortInUse -Port 8081) {
    Write-Error 'Port 8081 is already in use. Stop the existing backend before starting with Aiven.'
    exit 1
}

$defaultHost = 'mysql-36f4422f-ramyaveeramreddy2006-4ed3.c.aivencloud.com'
$defaultPort = '21882'
$defaultDatabase = 'defaultdb'
$defaultUsername = 'avnadmin'
$hostInput = (Read-Host "Aiven host [$defaultHost]").Trim()
$portInput = (Read-Host "Aiven port [$defaultPort]").Trim()
$databaseInput = (Read-Host "Aiven database name [$defaultDatabase]").Trim()
$usernameInput = (Read-Host "Aiven username [$defaultUsername]").Trim()
$env:AIVEN_HOST = if ($hostInput) { $hostInput } else { $defaultHost }
$portText = if ($portInput) { $portInput } else { $defaultPort }
$env:AIVEN_DATABASE = if ($databaseInput) { $databaseInput } else { $defaultDatabase }
$env:AIVEN_USERNAME = if ($usernameInput) { $usernameInput } else { $defaultUsername }
$securePassword = Read-Host 'Aiven password' -AsSecureString

$port = 0
if ([string]::IsNullOrWhiteSpace($env:AIVEN_HOST) -or
    $env:AIVEN_HOST.Contains('/') -or
    $env:AIVEN_HOST.Contains(':') -or
    -not [int]::TryParse($portText, [ref]$port) -or
    $port -lt 1 -or $port -gt 65535 -or
    [string]::IsNullOrWhiteSpace($env:AIVEN_DATABASE) -or
    [string]::IsNullOrWhiteSpace($env:AIVEN_USERNAME)) {
    Write-Error 'Invalid Aiven connection details. Enter the host, port, database name, and username shown in Aiven.'
    exit 1
}

$passwordPointer = [IntPtr]::Zero
$randomBytes = New-Object byte[] 32
$rng = [Security.Cryptography.RandomNumberGenerator]::Create()
try {
    $rng.GetBytes($randomBytes)
    $passwordPointer = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
    $env:AIVEN_PORT = "$port"
    $env:AIVEN_PASSWORD = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($passwordPointer)
    $env:JWT_SECRET = [Convert]::ToBase64String($randomBytes)
    $env:SPRING_PROFILES_ACTIVE = 'prod'
    $env:CORS_ALLOWED_ORIGINS = 'http://localhost:5173'
    Write-Host 'A temporary JWT secret was generated for this run. Existing sessions will need to sign in again after a restart.'

    Push-Location $PSScriptRoot
    try {
        & mvn spring-boot:run
        if ($LASTEXITCODE -ne 0) {
            exit $LASTEXITCODE
        }
    } finally {
        Pop-Location
    }
} finally {
    if ($passwordPointer -ne [IntPtr]::Zero) {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($passwordPointer)
    }
    [Array]::Clear($randomBytes, 0, $randomBytes.Length)
    $rng.Dispose()
    $env:AIVEN_HOST = $null
    $env:AIVEN_PORT = $null
    $env:AIVEN_DATABASE = $null
    $env:AIVEN_USERNAME = $null
    $env:AIVEN_PASSWORD = $null
    $env:JWT_SECRET = $null
    $env:SPRING_PROFILES_ACTIVE = $null
    $env:CORS_ALLOWED_ORIGINS = $null
    $securePassword.Dispose()
}
