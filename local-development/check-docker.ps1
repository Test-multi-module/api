$ErrorActionPreference = "Stop"

function Test-CommandAvailable {
    param ([string] $Command)
    return $null -ne (Get-Command $Command -ErrorAction SilentlyContinue)
}

function Test-DockerEngineReady {
    $previousErrorActionPreference = $ErrorActionPreference

    try {
        $ErrorActionPreference = "Continue"
        docker info 1>$null 2>$null
        return $LASTEXITCODE -eq 0
    }
    catch { return $false }
    finally { $ErrorActionPreference = $previousErrorActionPreference }
}

if (-not (Test-CommandAvailable "docker")) {
    throw "Docker CLI is not available. Please install Docker and make sure 'docker' is available in PATH."
}

docker --version
docker compose version 1>$null 2>$null

if ($LASTEXITCODE -ne 0) {
    throw "Docker Compose is not available. Please install Docker Compose plugin."
}

if (Test-DockerEngineReady) {
    Write-Host "Docker CLI, Docker Compose and Docker Engine are available."
    exit 0
}

Write-Host "Docker Engine is not running."

$dockerDesktopPath = "$Env:ProgramFiles\Docker\Docker\Docker Desktop.exe"

if (-not (Test-Path $dockerDesktopPath)) {
    throw "Docker Engine is not running. Docker Desktop was not found at: $dockerDesktopPath. Please start your Docker Engine manually and try again."
}

Write-Host "Trying to start Docker Desktop..."
Start-Process $dockerDesktopPath
Write-Host "Waiting for Docker Engine..."
$deadline = (Get-Date).AddSeconds(120)

while ((Get-Date) -lt $deadline) {
    if (Test-DockerEngineReady) {
        Write-Host "Docker Engine is ready."
        exit 0
    }

    Start-Sleep -Seconds 2
}

throw "Docker Engine did not become ready within timeout."