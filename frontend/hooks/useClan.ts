import { request } from "@/lib/request";
import { useQuery } from "@tanstack/react-query";

export const useMyClan = () =>
  useQuery({
    queryKey: ["my-clan"],
    queryFn: async () => {
      const res = await (
        await request("/users", {
          method: "GET",
        })
      ).json();
      console.log({ res });
      return res.inventory;
    },
  });
