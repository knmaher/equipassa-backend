<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <title>Reservation #${reservation.id}</title>
</head>

<body style="font-family: 'Arial', sans-serif; margin:0; padding:0; background-color:#f0f4f8;">

  <table role="presentation" style="width:100%;padding:20px 0;background-color:#f0f4f8;">
    <tr>
      <td align="center">
        <table role="presentation" style="width:600px;background:#ffffff;border-radius:10px;box-shadow:0 4px 8px rgba(0,0,0,0.1);padding:40px;">
          <tr>
            <td style="text-align:center;">
              <h2 style="color:#333;margin-bottom:10px;">Reservation Confirmed!</h2>
              <p style="color:#555;font-size:16px;">Hi <strong>${user.firstname}</strong>! Your reservation at <strong>${orgName}</strong> has been successfully confirmed.</p>
            </td>
          </tr>

          <tr>
            <td>
              <table role="presentation" style="width:100%;margin-top:20px;color:#555;font-size:15px;border-collapse:collapse;">
                <tr>
                  <td style="padding:10px;border-bottom:1px solid #e5e7eb;"><strong>Reservation ID:</strong></td>
                  <td style="padding:10px;border-bottom:1px solid #e5e7eb;">#${reservation.id}</td>
                </tr>
                <tr>
                  <td style="padding:10px;border-bottom:1px solid #e5e7eb;"><strong>Tool:</strong></td>
                  <td style="padding:10px;border-bottom:1px solid #e5e7eb;">${tool.name}</td>
                </tr>
                <tr>
                  <td style="padding:10px;border-bottom:1px solid #e5e7eb;"><strong>Quantity:</strong></td>
                  <td style="padding:10px;border-bottom:1px solid #e5e7eb;">${reservation.quantity}</td>
                </tr>
              </table>
            </td>
          </tr>

          <tr>
            <td style="text-align:center;padding-top:30px;">
              <a href="${portalUrl}/reservations/${reservation.id}" style="background-color:#2563eb;color:#ffffff;text-decoration:none;font-size:16px;padding:12px 30px;border-radius:8px;display:inline-block;">
                View Reservation
              </a>
            </td>
          </tr>

          <tr>
            <td style="padding-top:30px;color:#999;font-size:12px;text-align:center;">
              Please do not reply to this automated email.<br>
              Powered by <strong>Equipassa</strong>.
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>

</body>

</html>