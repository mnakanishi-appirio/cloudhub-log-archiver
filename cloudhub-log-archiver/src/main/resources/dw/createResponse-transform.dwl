%dw 2.0
output application/json
---
{
	"message": "Log Archive has been successfully finished.",
	"targets": vars.targetDomains as Array default []
}