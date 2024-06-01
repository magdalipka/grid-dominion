import { request } from "@/lib/request";
import { useQuery } from "@tanstack/react-query";

export const useInventory = () =>
  useQuery({
    queryKey: ["inventory"],
    queryFn: async () => {
      const res = (
        await request("/inventory", {
          method: "GET",
        })
      ).json();
    },
  });
