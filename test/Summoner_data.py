from riotwatcher import LolWatcher, ApiError
from riotwatcher._apis.league_of_legends import MatchApiV5


class Summoners():
    def __init__(self, api_key, region, ID):

        lol_watcher = LolWatcher(api_key)
        me = lol_watcher.summoner.by_name(region, ID)
        ranked_stats = lol_watcher.league.by_summoner(region, me['id'])
        self.ranked_stats = ranked_stats[0]


    def status(self):
        r = self.ranked_stats
        tier = r['tier']
        rank = r['rank']
        lp = r['leaguePoints']
        win_loss = [int(r['wins']), int(r['losses'])]


key = 'RGAPI-2011a811-a8fe-4526-8ec1-3bcf15adfb4b'
region = 'kr'
ID = 'hide on bush'
a = Summoners(key, region, ID)




