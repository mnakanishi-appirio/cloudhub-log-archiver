%dw 2.0
output multipart/form-data
var fileName = vars.instanceId as String ++ ".txt"
---
{
	"parts": {
		"file": {
			"headers": {
				"Content-Disposition": {
					"name": "file",
					"filename": fileName
				},
				"Content-Type": "application/octet-stream"
			},
			"content": vars.logDifference as String
		},
		"attributes": {
			"headers": {
				"Content-Type": "application/json"
			},
			"content": {
				"name": fileName,
				"parent": {
					"id": vars.domainFolderId as String
				}
			}
		}
	}	
}