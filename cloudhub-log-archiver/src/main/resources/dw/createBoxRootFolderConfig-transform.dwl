%dw 2.0
output application/json
import java!com::appirio::muleapp::util::CloudHubLogUtils
---
{
	"name": CloudHubLogUtils::getCurrentDateTimeStr() as String,
	"parent": {
		"id": 0
	}
}