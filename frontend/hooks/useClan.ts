import { request } from "@/lib/request";
import { useQuery } from "@tanstack/react-query";
import { useCurrentUser } from "./useUser";

export const useMyClan = () => {
  const { data: user } = useCurrentUser();

  return useQuery({
    queryKey: ["my-clan"],
    queryFn: async () => {
      const res = await (
        await request("/clans/" + user?.clanId, {
          method: "GET",
        })
      ).json();
      console.log({ res });
      return res;
    },
    enabled: Boolean(user?.clanId),
  });
};
