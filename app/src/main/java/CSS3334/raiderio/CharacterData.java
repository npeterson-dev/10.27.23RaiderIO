package CSS3334.raiderio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CharacterData extends LiveData<CharacterData>{

    private final MutableLiveData<CharacterData> data = new MutableLiveData<>();
    private final String name;
    private final String realm;
    private final String guildName;
    private final String spec;

    // mythic plus ranks
    private final int worldRankOverall;
    private final int regionRankOverall;
    private final int realmRankOverall;

    private final int worldRankClassDps;
    private final int regionRankClassDps;
    private final int realmRankClassDps;

    private final int worldRankClassHealer;
    private final int regionRankClassHealer;
    private final int realmRankClassHealer;

    public CharacterData(String name, String realm, String guildName, String spec,
                         int worldRankOverall, int regionRankOverall, int realmRankOverall,
                         int worldRankClassDps, int regionRankClassDps, int realmRankClassDps,
                         int worldRankClassHealer, int regionRankClassHealer, int realmRankClassHealer) {
        this.name = name;
        this.realm = realm;
        this.guildName = guildName;
        this.spec = spec;
        this.worldRankOverall = worldRankOverall;
        this.regionRankOverall = regionRankOverall;
        this.realmRankOverall = realmRankOverall;
        this.worldRankClassDps = worldRankClassDps;
        this.regionRankClassDps = regionRankClassDps;
        this.realmRankClassDps = realmRankClassDps;
        this.worldRankClassHealer = worldRankClassHealer;
        this.regionRankClassHealer = regionRankClassHealer;
        this.realmRankClassHealer = realmRankClassHealer;

        // setting the value of liveData using postValue
        data.postValue(this);
    }

    public LiveData<CharacterData> getData() {
        return data;
    }

    public String getName() {

        return name;
    }

    public String getRealm() {

        return realm;
    }

    public String getGuildName() {

        return guildName;
    }

    public String getSpec() {
        return spec;
    }

    // Getters for Mythic Plus Ranks
    public int getWorldRankOverall() {
        return worldRankOverall;
    }

    public int getRegionRankOverall() {
        return regionRankOverall;
    }

    public int getRealmRankOverall() {
        return realmRankOverall;
    }

    public int getWorldRankClassDps() {
        return worldRankClassDps;
    }

    public int getRegionRankClassDps() {
        return regionRankClassDps;
    }

    public int getRealmRankClassDps() {
        return realmRankClassDps;
    }

    public int getWorldRankClassHealer() {
        return worldRankClassHealer;
    }

    public int getRegionRankClassHealer() {
        return regionRankClassHealer;
    }

    public int getRealmRankClassHealer() {
        return realmRankClassHealer;
    }
}
