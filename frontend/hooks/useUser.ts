import { useAuth } from "@/contexts/auth";
import { request } from "@/lib/request";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

export const useCurrentUser = () => {
  return useQuery({
    queryKey: ["currentUser"],
    queryFn: async (): Promise<{ id: string; nick: string; clanId?: string }> => {
      const res = await (
        await request("/users", {
          method: "GET",
        })
      ).json();
      console.log({ currentUser: res });
      return res as { id: string; nick: string; clanId?: string };
    },
  });
};

export const useLogout = () => {
  const { logout } = useAuth();
  const client = useQueryClient();
  return useMutation({
    mutationFn: async () => {
      logout();
      return null;
    },
    onMutate: () => {
      client.invalidateQueries({ queryKey: ["currentUser"] });
      // client.refetchQueries({ queryKey: ["currentUser"] });
    },
  });
};
