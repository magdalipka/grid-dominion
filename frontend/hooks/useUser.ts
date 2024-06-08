import { request } from "@/lib/request";
import { useQuery } from "@tanstack/react-query";

export const useCurrentUser = () =>
  useQuery({
    queryKey: ["my-clan"],
    queryFn: async () => {
      const res = await (
        await request("/users", {
          method: "GET",
        })
      ).json();
      return res as { id: string; nick: string; clanId?: string };
    },
  });
