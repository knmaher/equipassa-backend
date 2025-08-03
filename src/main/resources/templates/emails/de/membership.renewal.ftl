<#function fmt dt>
  ${dt?string("d. MMM yyyy")}
</#function>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Mitgliedschaft Verlängert</title>
</head>
<body style="margin:0;padding:0;font-family:Arial,sans-serif;background:#f6f6f6;">
  <table role="presentation" width="100%" cellpadding="0" cellspacing="0">
    <tr>
      <td align="center" style="padding:24px 0;">
        <table role="presentation" width="600" cellpadding="0" cellspacing="0"
               style="background:#ffffff;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.05);">
          <tr>
            <td style="padding:32px;">
              <h2 style="margin:0 0 12px;color:#333;">Hallo&nbsp;${user.firstname}!</h2>
              <p style="margin:0 0 24px;color:#555;">
                Ihre Mitgliedschaft wurde erfolgreich <strong>verlängert</strong>.
              </p>
              <p style="margin:0 0 24px;color:#555;">
                Das neue Ablaufdatum ist der <strong>${fmt(newExpiry)}</strong>.
              </p>
              <p style="margin:0 0 24px;color:#555;">
                Vielen Dank, dass Sie Teil unserer Gemeinschaft sind!
              </p>
              <p style="margin:0;color:#888;font-size:12px;">
                Bereitgestellt von Equipassa • Bitte antworten Sie nicht auf diese automatische E-Mail.
              </p>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</body>
</html>