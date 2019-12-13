%dw 2.0
output application/json
---
{
	"name": vars.targetDomain as String,
	"parent": {
		"id": vars.todayFolderId as String
	}
}