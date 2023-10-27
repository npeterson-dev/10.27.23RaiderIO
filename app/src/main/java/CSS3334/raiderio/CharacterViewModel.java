package CSS3334.raiderio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.w3c.dom.CharacterData;

public class CharacterViewModel extends ViewModel {

    private final MutableLiveData<CharacterData> characterData = new MutableLiveData<>();

    public LiveData<CharacterData> getCharacterData() {
        return characterData;
    }

    public void setCharacterData(String name, String realm, String guildName, String spec,
                                 int worldRankOverall, int regionRankOverall, int realmRankOverall,
                                 int worldRankClassDps, int regionRankClassDps, int realmRankClassDps,
                                 int worldRankClassHealer, int regionRankClassHealer, int realmRankClassHealer) {
        CSS3334.raiderio.CharacterData data = new CSS3334.raiderio.CharacterData(name, realm, guildName, spec,
                worldRankOverall, regionRankOverall, realmRankOverall,
                worldRankClassDps, regionRankClassDps, realmRankClassDps,
                worldRankClassHealer, regionRankClassHealer, realmRankClassHealer);
        characterData.postValue((CharacterData) data);
    }
}
