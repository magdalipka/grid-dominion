import { request } from "@/lib/request";
import { useQuery } from "@tanstack/react-query";

export const useInventory = () =>
  useQuery({
    queryKey: ["myInventory"],
    queryFn: async () => {
      const res = await (
        await request("/inventories", {
          method: "GET",
        })
      ).json();
      console.log({ res });
      return res.inventoryHashMap as { WOOD: number; FOOD: number; GOLD: number };
    },
  });
