<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>E-Mail bestätigen</title>
</head>
<body style="margin:0;padding:0;font-family:Arial,sans-serif;background:#f6f6f6;">
  <table role="presentation" width="100%" cellpadding="0" cellspacing="0">
    <tr>
      <td align="center" style="padding:24px 0;">
        <table role="presentation" width="600" cellpadding="0" cellspacing="0"
               style="background:#ffffff;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.05);">
          <tr>
            <td style="padding:32px;">
              <h2 style="margin:0 0 12px;color:#333;">Hallo&nbsp;${adminName},</h2>
              <p style="margin:0 0 24px;color:#555;">
                Vielen Dank für die Registrierung Ihrer Organisation <strong>${organizationName}</strong>.
              </p>
              <p style="margin:0 0 24px;color:#555;">
                Bitte bestätigen Sie Ihre E-Mail-Adresse, indem Sie auf den folgenden Button klicken:
              </p>
              <div style="margin:24px 0;text-align:center;">
                <a href="${verificationLink}"
                   style="background:#2563eb;color:#fff;text-decoration:none;padding:12px 24px;border-radius:4px;">
                  E-Mail bestätigen
                </a>
              </div>
              <p style="margin:0 0 24px;color:#555;">
                Wenn Sie sich nicht registriert haben, ignorieren Sie bitte diese E-Mail.
              </p>
              <p style="margin:0;color:#888;font-size:12px;">
                Bereitgestellt von Equipassa • Bitte nicht auf diese automatische E-Mail antworten.
              </p>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</body>
</html>