package com.connectcrew.teamone.compositeservice.param;

import com.connectcrew.teamone.api.user.auth.Social;

public record RegisterParam(
        String token,
        Social social,
        String name,
        boolean termsAgreement,
        boolean privacyAgreement,
        boolean communityPolicyAgreement,
        boolean adNotificationAgreement
) {
}
