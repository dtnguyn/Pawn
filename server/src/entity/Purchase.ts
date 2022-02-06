import { Column, Entity, PrimaryColumn } from "typeorm";

@Entity()
export class Purchase {
  @PrimaryColumn()
  orderId: string;

  @Column()
  isValid: boolean;

  @Column()
  purchaseTime: string;

  @Column({ unique: true })
  purchaseToken: string;
}
