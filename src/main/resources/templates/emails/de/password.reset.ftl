<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Passwort zurücksetzen</title></head>
<body style="margin:0;padding:0;font-family:Arial,sans-serif;background:#f6f6f6;">
  <table role="presentation" width="100%" cellpadding="0" cellspacing="0">
  <tr><td align="center" style="padding:24px 0;">
    <table role="presentation" width="600" cellpadding="0" cellspacing="0"
           style="background:#fff;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.05);">
      <tr><td style="padding:32px;">
        <h2 style="margin:0 0 12px;color:#333;">Hallo ${user.firstname}!</h2>
        <p style="margin:0 0 24px;color:#555;">
          Wir haben eine Anfrage zum Zurücksetzen Ihres Passworts erhalten.
        </p>
        <div style="text-align:center;margin:24px 0;">
          <a href="${resetLink}"
             style="background:#2563eb;color:#fff;padding:12px 24px;
                    text-decoration:none;border-radius:4px;">
            Passwort zurücksetzen
          </a>
        </div>
        <p style="margin:0;color:#888;font-size:12px;">
          Wenn Sie das nicht angefordert haben, können Sie diese E-Mail ignorieren.
        </p>
        <p style="margin:24px 0 0;color:#888;font-size:12px;">
          Bereitgestellt von Equipassa • Bitte nicht antworten.
        </p>
      </td></tr>
    </table>
  </td></tr>
  </table>
</body>
</html>