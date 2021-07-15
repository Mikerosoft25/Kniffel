export interface Game {
  id: number,
  players: Player[];
  dices: Dice[];
  started: boolean;
  ended: boolean;
  restarted: boolean;
  playerTurn: number;
  rollCount: number;
  roundCount: number;
}

export interface Player {
  id?: number,
  name?: string,
  gameId?: number,
  host?: boolean,
  points?: number[],
  place?: number
}

export interface Dice {
  value : number;
  locked: boolean;
  index: number;
}

export interface TableCell {
  row: number;
  col: number;
}
