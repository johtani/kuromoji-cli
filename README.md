# Command Line Interface for Lucene Kuromoji

This plugin provide Command Line Interface for Lucene Kuromoji.

## Support Lucene version

* 8.4.1

## Build

### Requirements

* Gradle >= 6.0.1
* GraalVM >= java11-19.3.0 or JDK >= 1.8

### Build Native Image using JDK

**LIMITATION: Currently not support building stand-alone image...**

```
./gradle nativeImage
```

Then, gradle builds native command `kuromoji` in `build/graal` directory.

*NOTE:* `kuromoji` command needs `build/libs` directory, because this is a fallback image that requires a JDK for execution.


## Usage

### Basic usage

```
% kuromoji 関西国際空港限定トートバッグ
関西 関西国際空港 国際 空港 限定 トートバッグ
```

### Tokenize mode

`NORMAL`, `SEARCH`, `EXTENDED` can be specified. Default is `SEARCH`

```
% kuromoji -m=NORMAL 関西国際空港限定トートバッグ
関西国際空港 限定 トートバッグ
```

```
% kuromoji -m=EXTENDED 関西国際空港限定トートバッグ
関西 国際 空港 限定 ト ー ト バ ッ グ
```

### Output format

`wakati`, `mecab` can be specified. Default is `wakati`

```
% kuromoji -o=mecab 関西国際空港限定トートバッグ
関西    名詞,固有名詞,地域,一般,*,*,関西,カンサイ,カンサイ
関西国際空港    名詞,固有名詞,組織,*,*,*,関西国際空港,カンサイコクサイクウコウ,カンサイコクサイクーコー
国際    名詞,一般,*,*,*,*,国際,コクサイ,コクサイ
空港    名詞,一般,*,*,*,*,空港,クウコウ,クーコー
限定    名詞,サ変接続,*,*,*,*,限定,ゲンテイ,ゲンテイ
トートバッグ    名詞,一般,*,*,*,*,トートバッグ,*,*
```

## License

Apache License 2.0