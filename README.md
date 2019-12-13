# CloudHub Log Archiver
## 概要
```
Anypoint Platform の [Runtime Manager] - [Logs] で確認できるランタイムログを定期的に Box にアーカイブする Mule アプリケーションのサンプル実装です。
```

## 仕様
### Runtime properties
- `log.archive.environmentId` : Sandbox/Production などの環境を示す一意なUUID。（Hidden項目）
	- [Access Management] - [Environment] から取得する。
- `log.archive.excludeDomains` : カンマ区切りでApp名を複数指定。 
	- 本プロパティに指定したアプリケーション名(ドメイン名)はログアーカイブの対象外となる。
	- e.g. "sand-trainingdemo-app1,sand-trainingdemo-app2"
- `anypoint.login.username` : Anypoint platform のログインユーザ名。（Hidden項目）
	- Runtime Manager へのアクセス権が必要。
- `anypoint.login.password` : Anypoint platform のログインユーザのパスワード。（Hidden項目）
- `scheduler.frequency.hours` : スケジューラの定期実行の間隔。単位は hours
- `apiid` : API Manager にて取得した Autodiscovery 用の API IDを指定。

### 出力内容
- `log.archive.environmentId` に指定した環境配下の全てのAppのログが対象。
- 出力されるログは前回出力時の内容との差分のみ。
	- 差分がない場合はからファイルとして出力される。
- Boxに `<<your client app>>` アプリケーションとして、以下のディレクトリ構成でログを出力。
```
root - <yyyy-MM-dd-HHmmss> - <target domain> - <instanceId>.txt
                                             - <instanceId>.txt
```

### API
- エラー時に手動実行を行えるよう、APIのインターフェースを以下に定義しています。
	- `src/main/resources/api/api.raml`

### 備考
- BoxのJWT認証のため、クライアントアプリケーションを作成する必要があります。
	- [JWTアプリケーションの設定](https://ja.developer.box.com/docs/setting-up-a-jwt-app)
	- 生成した config.json を `box_access_config.json` に Rename し、`src/main/resources` 以下に配置してください。

- あくまで PoC の一環としての実装のため、以下の点は考慮していません。ご利用の際に適宜変更してください。
	- パフォーマンス
	- エラーハンドリング
	- リファクタリング
