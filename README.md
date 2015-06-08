# JSSE_Fortify_SCA_Rules

These rules identify issues in code relying on both JSSE and Apache HTTPClient since they are widely libraries for thick clients and Android apps.

-Over-Permissive Hostname Verifier: the rule is fired when the code declares a HostnameVerifier, and it always returns ‘true’.

-Over-Permissive Trust Manager: the rule is fired when the code declares a TrustManager and if it never throws a CertificateException. Throwing the exception is the way the API manages unexpected conditions.

-Missing Hostname Verification: the rule is fired when the code is using the Low-Level SSLSocket API and does not set a HostnameVerifier.

-Often Misused: Custom HostnameVerifier: the rule is fired when the code is using the High-Level HttpsURLConnection API and it sets a Custom HostnameVerifier. 

-Often Misused: Custom SSLSocketFactory: the rule is fired when the code is using the High-Level HttpsURLConnection API and it sets a Custom SSLSocketFactory.