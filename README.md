# Command Line Interface for Kuromoji (Atilika/Lucene)

This plugin provide Command Line Interface for Atilika Kuromoji and Lucene Kuromoji.


## Build

### Requirements

* JDK >= 21
* Gradle Wrapper (`./gradlew`) を利用

### Build Native Image using JDK

`nativeCompile` requires GraalVM JDK 21 with `native-image` installed.

PowerShell example:

```powershell
$env:GRAALVM_HOME = "C:\path\to\graalvm-jdk-21"
$env:JAVA_HOME    = $env:GRAALVM_HOME
$env:Path         = "$env:JAVA_HOME\bin;$env:Path"

gu install native-image
java -version
native-image --version
```

```
./gradlew nativeImage
```

Then, gradle builds native command `kuromoji` in `build/graal` directory.

If you get `native-image.cmd wasn't found`, your Gradle JVM is not GraalVM. Set `GRAALVM_HOME` / `JAVA_HOME` to GraalVM and retry.

#### Windows note (`vcvarsall.bat` error)

On Windows, `nativeCompile` also requires MSVC toolchain from Visual Studio 2022.

If you see:

```
Failed to find 'vcvarsall.bat' in a Visual Studio installation.
```

install **Visual Studio 2022 Build Tools** with:

* Desktop development with C++
* MSVC v143 - VS 2022 C++ x64/x86 build tools
* Windows 10/11 SDK

Then run in:

* `x64 Native Tools Command Prompt for VS 2022`

or from PowerShell:

```powershell
cmd /c """C:\Program Files\Microsoft Visual Studio\2022\BuildTools\VC\Auxiliary\Build\vcvars64.bat"" && gradlew.bat nativeCompile"
```

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

If `<filename>` is specified, `kuromoji` reads only the file and does not read from standard input.

#### Windows / PowerShell encoding note

`kuromoji` reads stdin and input files as UTF-8.

If PowerShell shows `????????` when piping Japanese text, set UTF-8 before execution:

```powershell
$OutputEncoding = [Console]::OutputEncoding = [System.Text.UTF8Encoding]::new($false)
[Console]::InputEncoding = [System.Text.UTF8Encoding]::new($false)
```

Example:

```powershell
echo "関西国際空港限定トートバッグ" | .\build\native\nativeCompile\kuromoji.exe
```

### Dictionary Type

`ipadic`, `unidic`, `naist_jdic`, `jumandic`, and `unidic_kanaaccent` can be specified. Default is `ipadic`.

### Engine

`atilika` and `lucene` can be specified by `-e` / `--engine`. Default is `atilika`.

If `lucene` is selected:

* `-d` / `--dictionary` is ignored with warning, and tokenization runs as ipadic-equivalent behavior.
* `-v` / `--viterbi` is not supported and emits warning.

### Tokenize mode

`NORMAL`, `SEARCH`, `EXTENDED` can be specified. Default is `SEARCH`.
*NOTE: With Atilika engine, `-m` is effective for `-d=ipadic`.*

```
% echo "関西国際空港限定トートバッグ" | kuromoji -m=NORMAL
関西国際空港 限定 トートバッグ
```

```
% echo "関西国際空港限定トートバッグ" | kuromoji -m=EXTENDED
関西 国際 空港 限定 ト ー ト バ ッ グ
```

### Output format

`wakati`, `mecab`, and `json` can be specified. Default is `wakati`

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

### Output Viterbi lattice as a DOT format file

Kuromoji allow to output Viterbi lattice and path as [DOT](https://en.wikipedia.org/wiki/DOT_(graph_description_language)) format.
This is debug purpose, but it is helpful to understand token outputs.
If `-v` or `--viterbi` option is specified with `--engine=atilika`, **the command outputs DOT file to stdout and outputs tokens to stderr.**
With `--engine=lucene`, this option is not supported and emits warning.

```sh
% echo "関西国際空港限定トートバッグ" | build/graal/kuromoji -v > viterbi.dot
```

#### Show dot file 

[Graphviz](http://www.graphviz.org/) is needed to convert DOT file to image file.
Run the below command, then output PNG file.

```sh
% echo "関西国際空港限定トートバッグ" | build/graal/kuromoji -v | dot -Tpng -oviterbi.png
```

If use MacOS, one line command is below: 
```sh
% echo "春眠暁を覚えず" | build/graal/kuromoji -v -o json | dot -Tpng | open -f -a preview.app
```

## License

Apache License 2.0
