# Command Line Interface for Lucene Kuromoji

This plugin provide Command Line Interface for Lucene Kuromoji.

## Support Lucene version

* 8.4.1

## Build

### Requirements

* Gradle >= 6.0.1
* JDK >= 12

### Build

```
gradle build
```

## Usage

*WIP*: Still work in progress. Stay tuned.

### Basic usage

```
% java -jar kuromoji-<Version>.jar 関西国際空港限定トートバッグ
関西 関西国際空港 国際 空港 限定 トートバッグ
```

### Tokenize mode

`NORMAL`, `SEARCH`, `EXTENDED` can be specified. Default is `SEARCH`

```
% java -jar kuromoji-<Version>.jar -m=NORMAL 関西国際空港限定トートバッグ
関西国際空港 限定 トートバッグ
```

```
% java -jar kuromoji-<Version>.jar -m=EXTENDED 関西国際空港限定トートバッグ
関西 国際 空港 限定 ト ー ト バ ッ グ
```

### Output format

`wakati`, `mecab` can be specified. Default is `wakati`

```
% java -jar kuromoji-<Version>.jar -o=mecab 関西国際空港限定トートバッグ
関西    名詞,固有名詞,地域,一般,*,*,関西,カンサイ,カンサイ
関西国際空港    名詞,固有名詞,組織,*,*,*,関西国際空港,カンサイコクサイクウコウ,カンサイコクサイクーコー
国際    名詞,一般,*,*,*,*,国際,コクサイ,コクサイ
空港    名詞,一般,*,*,*,*,空港,クウコウ,クーコー
限定    名詞,サ変接続,*,*,*,*,限定,ゲンテイ,ゲンテイ
トートバッグ    名詞,一般,*,*,*,*,トートバッグ,*,*
```

## License

Apache License 2.0