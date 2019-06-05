# Misskey Nest
MisskeyのためのAndroidクライアント（作りかけ）
## Description
これはMisskeyのためのAndroidクライアントです。これは公式のような名前をしていますが非公式クライアントです。
## Demo
簡単な投稿と、タイムラインの取得ができる。

## Usage
TimelineとReply Renote　Reactionと投稿はできる
使えたもんじ

## Requirement
###普通に使う場合
野良APKを公開しているので、ダウンロードする。
https://drive.google.com/drive/folders/1_VJjwzp5VDf09O7gZENhLVpoWalDVHWE
アプリを起動して、接続するインスタンスをタップして認証ボタンを押す。
権限の許可をすると勝手にリダイレクトしてタイムラインが表示される。

###自分でビルドする場合
まず
https://misskey.io/dev/apps　で自分のappSecretを作成する
※権限は全て選択すること。
次にpackage org.panta.misskey_nest.constant;パッケージに
abstractなApplicationConstantクラスを作成しそこにstaticなgetAppSecretKey()メソッドを作成する。
getAppSecretKey()の中に取得したappSecretをreturnするようにすればOK
※他人に配布する場合は必ずデコンパイルされたときにこのappSecretが盗まれないように難読化すること。
あとはビルドしてインストール

```
public abstract class ApplicationConstant {
    static String getAppSecretKey(){
        return "appSecret" //自分のAppSecret
    }
}
```

採用したアーキテクチャはMVP風の何かであるがMVPではない
ライセンス表記、著作権的に問題のあるファイルがあればすぐに報告してくださいよろしくお願いします。

usecaseパッケージやrepositoryパッケージがあるが正しい意味ではなく独自のものとしてとらえてほしい。


## install
https://drive.google.com/drive/folders/1_VJjwzp5VDf09O7gZENhLVpoWalDVHWE

## Licence
This application is open source software licensed under the Apache 2.0.

