# `DBテスト`と`その環境`について

----

## 最初に

+ 結果的に
  + 「出来た」けど「全然十全ではない」というものです…
  + 2016/09現在
+ これから育てていきたい(逃げ)

----

## やりたかったこと

+ 端末に依存しない「DB込の」テストの作成
+ 上記テストを均一かつ簡単に書くキャンバスの提供
+ CIで回す

----

## コンセプト

+ 出来るだけ作らない！！
    + 「世のスタンダードな何か」に全乗っかり！
+ 「在るモノ」を最大限に活用
    + 以前あったテストも蘇らせていく

----

## 今回使ったもの

+ Spring
+ DBUnit <span style="text-decoration: line-through;">(独自改造版)</span>
	+ → 三浦のプルリクが本家に取り入れられた！
      - これからは[コレ](https://mvnrepository.com/artifact/org.dbunit/dbunit/2.5.3)！
+ spring-test-dbunit(という名のプロダクト)
+ Docker

----

## 作ったもの

+ テスト書くためのスーパークラス
+ 上記を使ったいくつかのサンプル
+ 常時生存している「共有テスト用DB」(on Docker)
+ Jenkinsの「DBテスト」のJob


----

# 作り

----

## 大きな作り

図 -> TODO

----

## 考え方

図 -> TODO

+ 参照系

    + 基本的には「事後の条件のアサーション」はオブジェクトに対するチェック
        + Javaのユニットテストと変わらない
    + 事前条件だけ「DBの変更」が要る場合がある

+ 更新系

    + 事後の条件のアサーションは「DBのデータの状態に対するチェック」

----

## DBテストクラスの作り方、テストの書き方

+ アノテーションor特定のクラスを継承したものを「DBテスト用のクラス」と認識する
  + `DatabaseTestCase` を継承する
  + `@Category(DatabaseTests.class)` つける
+ 普通に `@Test` なメソッド書く
+ 「事前放り込みデータ」や「事後確認用データ」は、メソッドにアノテーションでExcelファイル名を指定


----

## その他「中身」や「細かい話」

+ テスト系の設定は”applicationContext-test.xml”
+ テスト開始時に…
  - 「参照整合性をOffにしてデータ投入後再びOnにする」
    という処理をしている


----

## 共有テストDB

<strong>jdbc:mysql://localhost:3306/stda</strong>

※Docker上の常時起動DBサーバ

----

## 「共有テストDB」の考え方

+ 「Javaの重鎮たちが出してる声明リスト(某ページから拝借)」を基本に
+ 基本的には読み取り専用
+ 変更しても良いが…
  0. 「周知」
  0. 「既存テストをこけささない（こかした人が整える）」を確認

+ 一日ごとに「データのエクスポート結果」をgitに取っている
  + 大破壊があっても結構気軽に戻せる


----

## デモ

0. 空のクラスを作る
0. `DatabaseTestCase` を継承する
0. `sut` と その初期化ソースを足す
0. テストケース一つ目を作る
0. 「参照系」「DBにすでにある」だけなら「何もせずに実行＋assert」だけで書ける
0. 「特殊なデータの取得」なら「事前データを入れたExcelをアノテーションにかいて」始める
0. 「更新系」「DBにすでにある」だけなら「事後条件（データ）を書いたExcelを用意しアノテーションに指定」
0. 「更新系」で「特殊なデータの事前条件」なら「事前データを入れたExcelをアノテーションにかいて」対応

----

## 積み残し

(今日までにやる予定だったが出来てないもの)

+ マイグレーションを単体で走らせるように
  + 「CI側テスト開始時」など「コマンドで任意タイミング」に

ちょっとずつやる！

----

## 既知の課題

+ 直せそうもない系
  + IDE(Eclipse)で「全部回して！」とかやった時にDBテストも回る
    + 引数で指定しても `Category` を認識してくれない
  + 共有テスト用DBのメンテのやり方もルールも甘甘
  + 「実行中のDB状態の確認」がやりにくい…
  + 排他の考慮がございません
  + トランザクション二つに対応できない
+ これを使って「テストを書いてく」場合の運用系
    + 単体テストの実行の開始が遅い
    + 更新系のアサートが多分「壊れやすいテスト」


----

## 本当は

壊れやすいテストには…


```java:Test.java
// act
actual = ほにゃらら;

// assertion
String expect = exec("SELECT FLD1 FROM MST1 WHERE KEY = 1;").get(0).col("FLD1");
assertThat(actual , is(expect));

```


くらいの簡易さで書きたい！

----

## 将来

+ 積み残しの片付け
+ 「プロダクトのJavaプロジェクト本体」との切り離し
  + 「テストだけ」で回せるように
  + 実行時に引用or混ぜるような概念が要る？
+ `環境依存DB/共通DB` で「テスト時自動テストベッド用意」機構
  + 「MySQLのDBを自ら持ってきて立ち上げてテスト終わったら死んでいく」機構にできたら嬉しい
+ `環境依存DB/共通DB` の「簡易メンテナンス」方法の提供

----

## 知っておいてほしいこと

+ 手段は一つではない
    + たとえば「Excel書くのとか嫌！別の手段でやりたい」と思うなら、俺もそっちが良いと思うし
+ 「縛るレールや脱走防止柵を作りたい」わけじゃない、 「広大なエリアを縦横無尽に行ける道具を提供したい」んだ

----

## おわり

+ ご要望またはアイディアあれば随時下さい！