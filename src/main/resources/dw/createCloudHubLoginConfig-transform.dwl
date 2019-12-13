%dw 2.0
output application/json
var username = p('anypoint.login.username')
var password = p('anypoint.login.password')
---
{
	"username": username,
	"password": password
}