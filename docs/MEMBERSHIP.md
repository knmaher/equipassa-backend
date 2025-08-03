# Subscription & Billing

## Overview

Each **Organization** in Equipassa subscribes to one of our three Subscription Tiers—Free, Premium, or Enterprise. This
subscription controls which features are available, how many tools you may manage, and billing/renewal policies at the
organization level.

## Subscription Tiers

| Tier     | Price      | Tool Limit | Key Features                         |
|----------|------------|------------|--------------------------------------|
| **Free** | $0 / month | 100        | • Basic reservations & cancellations |

• Manual check-in/out  
• Simple availability checks  
• Basic audit logs  
• JWT auth & email‐reset  
| **Premium** | $99 / month | 1 000 | All Free, plus:  
• Automated renewal reminders & payments (Stripe/PayPal)  
• Detailed item catalog (photos, descriptions)  
• Wait-list & conflict resolution  
• Email/SMS notifications  
• Barcode/QR check-in/out  
• Auto overdue notices & billing  
• Usage analytics dashboards  
| **Enterprise** | Custom pricing | Unlimited | All Premium, plus:  
• Multi-location & RFID/IoT integration  
• Accounting integrations (QuickBooks, Xero)  
• SLA & white-labeling  
• Advanced compliance & security (IP restrictions, MFA)  
• Custom reporting & API access

---

## Subscription Lifecycle

1. **Activation**
    - Upon organization creation, the founder selects a tier.
    - The org’s `subscriptionExpiryDate` is set (today + billing cycle).

2. **Renewal**
    - **Automatic** (Premium/Enterprise): system charges on `subscriptionExpiryDate – 7 days`.
    - **Manual** (Free & fallback): Org Admin clicks **Renew** in the UI or calls the API.
    - On success, `subscriptionExpiryDate` extends by one billing period.
    - Fires `MembershipRenewedEvent` to trigger notifications.

3. **Expiration**
    - If payment fails or no renewal before expiry:
        - Downgrade features → Free tier.
        - Notify Org Admin via email (“Your subscription expired on …”).

---

## Roles & Permissions

- **ORG_ADMIN**
    - Manages the organization’s subscription (renew, upgrade/downgrade).
    - Invites internal staff & grants roles.

- **ADMIN / STAFF**
    - Use features granted by the org’s tier.
    - Cannot modify the org subscription.

- **USER**
    - Can make reservations/check-outs within org rules.

---

## API Endpoints

### GET /api/org/subscription

Fetch current tier and expiry date.

```http
GET /api/org/subscription
Authorization: Bearer <token>
