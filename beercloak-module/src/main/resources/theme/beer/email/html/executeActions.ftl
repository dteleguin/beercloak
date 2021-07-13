<#ftl output_format="plainText">
<html>
<head>
<meta charset="UTF-8" />
          <title>${msg("welcomeMailTitle")}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width"/>
    <style type="text/css">
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400&amp;display=swap');
* { margin: 0; padding: 0; font-size: 100%; font-family: 'Roboto', Arial, Helvetica, sans-serif; line-height: 1.65; }
img { max-width: 100%; margin: 0 auto; display: block; }
body, .body-wrap { width: 100% !important; height: 100%; background: #f8f8f8; }
a { color: #5daade; text-decoration: none; }
a:hover { text-decoration: underline; }
.text-center { text-align: center; }
.text-right { text-align: right; }
.text-left { text-align: left; }
.button { display: inline-block; color: white; background: #5daade; border: solid #5daade; border-width: 10px 20px 8px; font-weight: bold; border-radius: 100px; }
.button:hover { text-decoration:  ; }
h2, h3 { margin-bottom: 10px; line-height: 1.25; }
h2 { font-size: 20px; }
h3 { font-size: 19px; margin-bottom: 0; }
p, ul, ol { font-size: 19px; font-weight: normal; margin-bottom: 17px; }
.container { display: block !important; clear: both !important; margin: 0 auto !important; max-width: 580px !important; }
.container table { width: 100% !important; border-collapse: collapse; }
.container .masthead { padding: 20px 0; background: white; color: white; }
.container .masthead h1 { margin: 0 auto !important; max-width: 90%; text-transform: ; }
.container .content { background: white; padding: 30px 35px; }
.container .content.footer { background: none; }
.container .content.footer p { margin-bottom: 0; color: #888; text-align: center; font-size: 14px; }
.container .content.footer a { color: #888; text-decoration: none; font-weight: bold; }
.container .content.footer a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<table class="body-wrap">
    <tr>
        <td class="container">
            <table>
                <tr>
                    <td align="center" class="masthead">
                        <img alt="intension logo" align="middle" style="padding-left: 10px; width: 200px; height: auto" src="data:image/png;base64,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" />
                    </td>
                </tr>
                <tr>
                    <td class="content">
                       ${msg("welcomeMailHtml",link, linkExpiration, realmName, linkExpirationFormatter(linkExpiration))} 
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td class="container">
            <table>
                <tr>
                    <td class="content footer" align="center">
                        ${msg("welcomeMailFooterHtml")}
                        <p><a href="mailto: info@intension.de">info@intension.de</a></p>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>