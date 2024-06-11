import React from "react";
import { request } from "@/lib/request";

const AuthContext = React.createContext<{
  isLoading: boolean;
  user: { nick: string } | null;
  login: (_: { nick: string; password: string }) => Promise<void>;
  logout: () => Promise<void>;
  register: (_: { nick: string; password: string }) => void;
}>({
  isLoading: true,
  user: null,
  login: function (_: { nick: string; password: string }): Promise<void> {
    throw new Error("Function not implemented.");
  },
  register: function (_: { nick: string; password: string }): void {
    throw new Error("Function not implemented.");
  },
  logout: function (): Promise<void> {
    throw new Error("Function not implemented.");
  },
});

const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [isLoading, setIsLoading] = React.useState(true);
  const [session, setSession] = React.useState<{ nick: string } | null>(null);
  const [credentials, setCredentials] = React.useState<{
    nick: string;
    password: string;
  } | null>({ nick: "", password: "" });

  const initSession = React.useCallback(async () => {
    try {
      const res = await (
        await request("/users", {
          method: "GET",
        })
      ).json();
      if (res.id) {
        setSession(res);
      }
    } catch (error) {
      console.error({ error });
    }
    setIsLoading(false);
  }, []);

  const register = React.useCallback(
    async ({ nick, password }: { nick: string; password: string }) => {
      const res = await (
        await request("/users", {
          method: "POST",
          headers: {
            "content-type": "application/json",
          },
          body: JSON.stringify({
            nick,
            password,
          }),
        })
      ).json();
      if (!res.error) {
        setSession(res);
        console.log({ res });
      }
      setIsLoading(false);
      setCredentials({ nick, password });
    },
    []
  );

  const login = React.useCallback(
    async ({ nick, password }: { nick: string; password: string }) => {
      const res = await (
        await request("/users/login", {
          method: "POST",
          headers: {
            authorization: "Basic " + btoa(nick + ":" + password),
          },
        })
      ).json();
      if (!res.error) {
        setSession(res);
        console.log({ res });
      }
      setCredentials({ nick, password });
      setIsLoading(false);
    },
    []
  );

  const logout = React.useCallback(async () => {
    const res = await (
      await request("/users/logout", {
        method: "POST",
      })
    ).json();
    if (!res.error) {
      console.log({ res });
    }
    setSession(null);
    setCredentials(null);
    setIsLoading(false);
  }, []);

  React.useEffect(() => {
    initSession();
  }, []);

  return (
    <AuthContext.Provider value={{ isLoading, user: session, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

const useAuth = () => React.useContext(AuthContext);

export { AuthProvider, useAuth };
