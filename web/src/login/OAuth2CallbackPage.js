import React from "react";
import { Navigate } from "react-router-dom";
import { exchangeGoogleCode } from "./api/OAuth2RestClient";

class OAuth2CallbackPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            redirect: false,
            error: null
        };
        this.handleTokenResponse = this.handleTokenResponse.bind(this);
    }

    componentDidMount() {
        const params = new URLSearchParams(window.location.search);
        const code = params.get("code");

        if (!code) {
            this.setState({ error: "Missing authorization code." });
            return;
        }

        const redirectUri = window.location.origin + "/oauth2/callback";
        exchangeGoogleCode({ code, redirectUri }, this.handleTokenResponse);
    }

    handleTokenResponse(response, status) {
        if (status === 200) {
            window.localStorage.setItem("token", response.token);
            window.localStorage.setItem("id", response.id);
            this.setState({ redirect: true });
        } else {
            this.setState({ error: "Google sign-in failed. Please try again." });
        }
    }

    render() {
        if (this.state.redirect) return <Navigate to="/dashboard" />;
        if (this.state.error) {
            return (
                <div style={{ textAlign: "center", marginTop: "80px" }}>
                    <p>{this.state.error}</p>
                    <a href="/login">Back to login</a>
                </div>
            );
        }
        return (
            <div style={{ textAlign: "center", marginTop: "80px" }}>
                <p>Signing you in with Google…</p>
            </div>
        );
    }
}

export default OAuth2CallbackPage;
