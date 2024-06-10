import { useAuth } from "@/contexts/auth";
import { request } from "@/lib/request";
import { useQuery } from "@tanstack/react-query";

export const useCurrentUser = () => {
  const { login, credentials } = useAuth();
  return useQuery({
    queryKey: ["currentUser"],
    queryFn: async (): Promise<{ id: string; nick: string; clanId?: string }> => {
      try {
        const res = await (
          await request("/users", {
            method: "GET",
          })
        ).json();
        console.log({ currentUser: res });
        return res as { id: string; nick: string; clanId?: string };
      } catch {
        await login(credentials);
        const res = await (
          await request("/users", {
            method: "GET",
          })
        ).json();
        console.log({ currentUser: res });
        return res as { id: string; nick: string; clanId?: string };
      }
    },
  });
};
