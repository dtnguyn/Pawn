import { Purchase } from "../entity/Purchase";
import { getRepository } from "typeorm";
import { User } from "../entity/User";

const isPurchaseValid = async (token: string) => {
  const repo = getRepository(Purchase);

  const results = await repo.find({ purchaseToken: token });

  return results.length == 0;
};

export const purchasePremium = async (
  token: string,
  time: string,
  orderId: string
) => {
  const result = await isPurchaseValid(token);
  console.log("Purchase isValid", result);
  if (result) {
    //Make a purchase

    const repo = getRepository(Purchase);

    await repo.insert({
      orderId,
      purchaseTime: time,
      isValid: true,
      purchaseToken: token,
    });

    return true;
  } else {
    //Return false
    return false;
  }
};
