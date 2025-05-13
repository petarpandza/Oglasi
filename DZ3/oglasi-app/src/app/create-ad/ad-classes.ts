export class adInfo {
    lobbyName: string;
    lobbyID: string;
    game: string;
    filled: boolean;

    constructor(name: string, id: string, game: string){
        this.lobbyName = name;
        this.lobbyID = id;
        this.game = game;
        this.filled = false;
    }
}
