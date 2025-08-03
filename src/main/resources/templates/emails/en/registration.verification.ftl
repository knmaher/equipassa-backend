<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Verify Your Email</title>
</head>
<body style="margin:0;padding:0;font-family:Arial,sans-serif;background:#f6f6f6;">
  <table role="presentation" width="100%" cellpadding="0" cellspacing="0">
    <tr>
      <td align="center" style="padding:24px 0;">
        <table role="presentation" width="600" cellpadding="0" cellspacing="0"
               style="background:#ffffff;border-radius:8px;box-shadow:0 2px 6px rgba(0,0,0,0.05);">
          <tr>
            <td style="padding:32px;">
              <h2 style="margin:0 0 12px;color:#333;">Hi&nbsp;${adminName},</h2>
              <p style="margin:0 0 24px;color:#555;">
                Thank you for registering your organization <strong>${organizationName}</strong>.
              </p>
              <p style="margin:0 0 24px;color:#555;">
                Please verify your email address by clicking the button below:
              </p>
              <div style="margin:24px 0;text-align:center;">
                <a href="${verificationLink}"
                   style="background:#2563eb;color:#fff;text-decoration:none;padding:12px 24px;border-radius:4px;">
                  Verify Email
                </a>
              </div>
              <p style="margin:0 0 24px;color:#555;">
                If you did not register, please ignore this email.
              </p>
              <p style="margin:0;color:#888;font-size:12px;">
                Powered by Equipassa • Please do not reply to this automated email.
              </p>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</body>
</html>