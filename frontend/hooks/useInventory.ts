import { request } from "@/lib/request";
import { useQuery } from "@tanstack/react-query";

export const useInventory = () =>
  useQuery({
    queryKey: ["inventory"],
    queryFn: async () => {
      const res = await (
        await request("/inventories", {
          method: "GET",
        })
      ).json();
      return res.inventoryHashMap as { WOOD: number; FOOD: number; GOLD: number };
    },
  });
