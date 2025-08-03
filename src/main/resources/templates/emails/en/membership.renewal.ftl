<#function fmt dt>
  ${dt?string("d MMM yyyy")}
</#function>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Membership Renewed</title>
</head>
<body style="margin:0;padding:0;font-family:Arial,sans-serif;background:#f6f6f6;">
  <table role="presentation" width="100%" cellpadding="0" cellspacing="0">
    <tr>
      <td align="center" style="padding:24px 0;">
        <table role="presentation" width="600" cellpadding="0" cellspacing="0"
               style="background:#ffffff;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.05);">
          <tr>
            <td style="padding:32px;">
              <h2 style="margin:0 0 12px;color:#333;">Hi&nbsp;${user.firstname}!</h2>
              <p style="margin:0 0 24px;color:#555;">
                Your membership has been <strong>renewed</strong> successfully.
              </p>
              <p style="margin:0 0 24px;color:#555;">
                Your new expiration date is <strong>${fmt(newExpiry)}</strong>.
              </p>
              <p style="margin:0 0 24px;color:#555;">
                Thank you for being part of our community!
              </p>
              <p style="margin:0;color:#888;font-size:12px;">
                Powered by Equipassa • Please do not reply to this automated e-mail.
              </p>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</body>
</html>