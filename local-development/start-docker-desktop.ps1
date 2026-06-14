$ErrorActionPreference = "Stop"

function Test-DockerReady {
    $previousErrorActionPreference = $ErrorActionPreference

    try {
         $ErrorActionPreference = "Continue"
         docker info 1>$null 2>$null
         return $LASTEXITCODE -eq 0
    }
     catch {
        return $false
     }
     finally {
        $ErrorActionPreference = $previousErrorActionPreference
     }
}

if (Test-DockerReady) {
    Write-Host "Docker Engine is already running."
    exit 0
}

Write-Host "Docker Engine is not running. Trying to start Docker Desktop..."

$dockerDesktopPath = "$Env:ProgramFiles\Docker\Docker\Docker Desktop.exe"

if (-not (Test-Path $dockerDesktopPath)) {
    throw "Docker Desktop executable was not found at: $dockerDesktopPath"
}

Start-Process $dockerDesktopPath

Write-Host "Waiting for Docker Engine..."

$deadline = (Get-Date).AddSeconds(120)

while ((Get-Date) -lt $deadline) {
    if (Test-DockerReady) {
        Write-Host "Docker Engine is ready."
        exit 0
    }

    Start-Sleep -Seconds 2
}

throw "Docker Engine did not become ready within timeout."