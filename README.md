# Command Line Interface for Atilika Kuromoji

This plugin provide Command Line Interface for Atilika Kuromoji.


## Build

### Requirements

* Gradle >= 6.0.1
* GraalVM >= java11-19.3.0 or JDK >= 1.8

### Build Native Image using JDK

```
./gradle nativeImage
```

Then, gradle builds native command `kuromoji` in `build/graal` directory.

## Usage

### Basic usage

Text from standard input:

```
% echo "関西国際空港限定トートバッグ" | kuromoji
関西 関西国際空港 国際 空港 限定 トートバッグ
```

Also the file can be specified as a parameter.

```
% kuromoji <filename>
関西 関西国際空港 国際 空港 限定 トートバッグ
```

### Dictionary Type

`ipadic`, `unidic`, `naist_jdic`, `jumandic`, and `unidic_kanaaccent` can be specified. Default is `ipadic`.

### Tokenize mode

`NORMAL`, `SEARCH`, `EXTENDED` can be specified. Default is `SEARCH`.
*NOTE: This option can only use with `-d=ipadic`.*

```
% echo "関西国際空港限定トートバッグ" | kuromoji -m=NORMAL
関西国際空港 限定 トートバッグ
```

```
% echo "関西国際空港限定トートバッグ" | kuromoji -m=EXTENDED
関西 国際 空港 限定 ト ー ト バ ッ グ
```

### Output format

`wakati`, `mecab` can be specified. Default is `wakati`

```
% echo "関西国際空港限定トートバッグ" | kuromoji -o=mecab
関西    名詞,固有名詞,地域,一般,*,*,関西,カンサイ,カンサイ
関西国際空港    名詞,固有名詞,組織,*,*,*,関西国際空港,カンサイコクサイクウコウ,カンサイコクサイクーコー
国際    名詞,一般,*,*,*,*,国際,コクサイ,コクサイ
空港    名詞,一般,*,*,*,*,空港,クウコウ,クーコー
限定    名詞,サ変接続,*,*,*,*,限定,ゲンテイ,ゲンテイ
トートバッグ    名詞,一般,*,*,*,*,トートバッグ,*,*
EOS
```

## License

Apache License 2.0