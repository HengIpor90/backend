$uri = [Environment]::GetEnvironmentVariable('MONGODB_URI','User')
if (-not $uri) {
    Write-Error 'User MONGODB_URI not set'
    exit 1
}
Write-Output 'Starting mvnw with spring.data.mongodb.uri from user env (secret suppressed)'
Set-Location "$PSScriptRoot\.."
## Export as an environment variable that Spring will pick up (DOT -> UNDERSCORE mapping)
$env:SPRING_DATA_MONGODB_URI = $uri
& .\mvnw.cmd -DskipTests spring-boot:run
