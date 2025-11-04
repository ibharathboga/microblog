import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Search as SearchIcon } from 'lucide-react';
import { Layout } from '@/components/Layout';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { Avatar, AvatarFallback } from '@/components/ui/avatar';
import { usersAPI, followsAPI } from '@/lib/api';
import { toast } from 'sonner';

interface UserResult {
  id: string;
  username: string;
  isFollowed?: boolean;
}

export default function Search() {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState<UserResult[]>([]);
  const [loading, setLoading] = useState(false);

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!query.trim()) return;

    setLoading(true);
    try {
      const users = await usersAPI.searchUsers(query);
      setResults(users);
    } catch (error) {
      toast.error('Failed to search users');
    } finally {
      setLoading(false);
    }
  };

  const handleFollowToggle = async (userId: string, isFollowed: boolean) => {
    try {
      if (isFollowed) {
        await followsAPI.unfollowUser(userId);
      } else {
        await followsAPI.followUser(userId);
      }
      
      setResults((prev) =>
        prev.map((user) =>
          user.id === userId ? { ...user, isFollowed: !isFollowed } : user
        )
      );
      
      toast.success(isFollowed ? 'Unfollowed' : 'Following');
    } catch (error) {
      toast.error('Failed to update follow status');
    }
  };

  return (
    <Layout>
      <div className="border-x border-border min-h-screen">
        <div className="sticky top-0 z-10 border-b border-border bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
          <h1 className="p-4 text-xl font-bold">Search</h1>
        </div>

        <div className="p-4">
          <form onSubmit={handleSearch} className="flex space-x-2">
            <div className="relative flex-1">
              <SearchIcon className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
              <Input
                type="text"
                placeholder="Search users..."
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                className="pl-10"
              />
            </div>
            <Button type="submit" disabled={loading}>
              {loading ? 'Searching...' : 'Search'}
            </Button>
          </form>
        </div>

        <div className="divide-y divide-border">
          {results.length === 0 && query && !loading && (
            <div className="flex flex-col items-center justify-center py-16 text-center">
              <p className="text-muted-foreground">No users found</p>
            </div>
          )}

          {results.map((user) => (
            <div key={user.id} className="flex items-center justify-between p-4">
              <Link to={`/user/${user.username}`} className="flex items-center space-x-3 flex-1">
                <Avatar>
                  <AvatarFallback className="bg-primary text-primary-foreground">
                    {user.username[0].toUpperCase()}
                  </AvatarFallback>
                </Avatar>
                <span className="font-medium">@{user.username}</span>
              </Link>
              <Button
                size="sm"
                variant={user.isFollowed ? 'outline' : 'default'}
                onClick={() => handleFollowToggle(user.id, user.isFollowed || false)}
              >
                {user.isFollowed ? 'Unfollow' : 'Follow'}
              </Button>
            </div>
          ))}
        </div>
      </div>
    </Layout>
  );
}
