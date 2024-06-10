import { request } from "@/lib/request";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useEffect } from "react";

export type Building = {
  id: string;
  level: number;
  goldCost: number;
  woodCost: number;
  foodCost: number;
  bonus: number;
  type: string;
};

export const useUpgradeBuilding = () => {
  const client = useQueryClient();
  return useMutation({
    mutationFn: async ({ buildingId }: { buildingId: string }) => {
      const res = await (
        await request("/buildings/" + buildingId + "/upgrade", {
          method: "POST",
          headers: {
            "content-type": "application/json",
          },
        })
      ).json();
      console.log({ res });
      return res;
    },
    onMutate: () => {
      client.refetchQueries({ queryKey: ["territories"] });
      client.refetchQueries({ queryKey: ["myInventory"] });
    },
  });
};
