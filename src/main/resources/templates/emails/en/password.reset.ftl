<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Password Reset</title></head>
<body style="margin:0;padding:0;font-family:Arial,sans-serif;background:#f6f6f6;">
  <table role="presentation" width="100%" cellpadding="0" cellspacing="0">
  <tr><td align="center" style="padding:24px 0;">
    <table role="presentation" width="600" cellpadding="0" cellspacing="0"
           style="background:#fff;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.05);">
      <tr><td style="padding:32px;">
        <h2 style="margin:0 0 12px;color:#333;">Hi ${user.firstname}!</h2>
        <p style="margin:0 0 24px;color:#555;">
          We received a request to reset your password. Click the button below to set a new one:
        </p>
        <div style="text-align:center;margin:24px 0;">
          <a href="${resetLink}"
             style="background:#2563eb;color:#fff;padding:12px 24px;
                    text-decoration:none;border-radius:4px;">
            Reset Password
          </a>
        </div>
        <p style="margin:0;color:#888;font-size:12px;">
          If you didn’t ask for a password reset, just ignore this email.
        </p>
        <p style="margin:24px 0 0;color:#888;font-size:12px;">
          Powered by Equipassa • Please do not reply.
        </p>
      </td></tr>
    </table>
  </td></tr>
  </table>
</body>
</html>